package org.example;


import org.example.core.configuration.JPAService;
import org.example.model.ProgrammingLanguage;

import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {



        private static final JPAService jpaService = JPAService.getInstance();

        public static void main(String[] args) {

            try {

                createProgrammingLanguages();
                printTopProgrammingLanguages();

            } finally {
                jpaService.shutdown();
            }
        }

        private static void createProgrammingLanguages() {
            jpaService.runInTransaction(entityManager -> {
                Arrays.stream("Java,C++,C#,JavaScript,Rust,Go,Python,PHP".split(","))
                        .map(name -> new ProgrammingLanguage(name, (int) (Math.random() * 10)))
                        .forEach(entityManager::persist);
                return null;
            });
        }

        private static void printTopProgrammingLanguages() {
            List<ProgrammingLanguage> programmingLanguages = jpaService.runInTransaction(entityManager ->
                    entityManager.createQuery(
                            "select p from ProgrammingLanguage p where p.rating > 5",
                            ProgrammingLanguage.class
                    ).getResultList());

            programmingLanguages.stream()
                    .map(pl -> pl.getName() + ": " + pl.getRating())
                    .forEach(System.out::println);
        }



}