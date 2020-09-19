package com.misolova.medifora.util

import com.misolova.medifora.domain.model.Answer
import com.misolova.medifora.domain.model.Question

class TestData {
    val questionsArrayList: ArrayList<Question> =
        arrayListOf(
            Question(
                111,
                "Fake Question Content",
                arrayListOf(333, 444),
                2,
                "Collins",
                "12/08/2010"
            ),
            Question(
                222,
                "Testing Question Content",
                arrayListOf(555, 666, 777),
                3,
                "John",
                "20/03/2020"
            ),
            Question(
                0,
                "Test Question Without Answers",
                arrayListOf(),
                0,
                "Fred",
                "10/02/2020"
            )
        )

    val userAnswersArrayList: ArrayList<Answer> =
        arrayListOf(
            Answer(
                333,
                "First Fake Answer Content",
                "Fake Question Content",
                111,
                "Jane",
                "12/08/2014"
            ),
            Answer(
                444,
                "Second Testing Answer Content",
                "Fake Question Content",
                111,
                "John",
                "21/01/2017"
            ),
            Answer(
                555,
                "Third Testing Answer Content",
                "Testing Question Content",
                222,
                "Taraji",
                "21/05/2020"
            ),
            Answer(
                666,
                "Fourth Testing Answer Content",
                "Testing Question Content",
                222,
                "Mike",
                "17/06/2020"
            ),
            Answer(
                777,
                "Fifth Testing Answer Content",
                "Testing Question Content",
                222,
                "Fred",
                "12/12/2019"
            )
        )

}