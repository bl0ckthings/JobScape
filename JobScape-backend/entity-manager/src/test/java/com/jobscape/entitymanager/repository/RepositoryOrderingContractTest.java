package com.jobscape.entitymanager.repository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryOrderingContractTest {

    @Test
    void remindersExposeOldestFirstApplicationQuery() throws NoSuchMethodException {
        assertEquals(
                List.class,
                ReminderRepository.class.getMethod(
                        "findByApplication_IdOrderByReminderDateAscIdAsc",
                        Long.class
                ).getReturnType()
        );
    }

    @Test
    void applicationStepsExposeNewestFirstApplicationQuery() throws NoSuchMethodException {
        assertEquals(
                List.class,
                ApplicationStepRepository.class.getMethod(
                        "findByApplication_IdOrderByStepDateDescIdDesc",
                        Long.class
                ).getReturnType()
        );
    }
}
