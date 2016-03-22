package com.sahaj.wikiproblem;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnswerMatcherTest {

    String[] jobsAnswer = {
            "to travel through India",
            "“one of the two or three most important things” he did in his life",
            "older engineering student (and Homestead High alumnus) Wozniak and his girlfriend, the artistically inclined and countercultural Homestead High junior Chrisann Brennan",
            "San Francisco",
            "Bob Dylan",
    };
    String[] zebraAnswer = {
            "Grévy's zebra and the mountain zebra",
            "aims to breed zebras that are phenotypically similar to the quagga",
            "horses and donkeys",
            "the plains zebra, the Grévy's zebra and the mountain zebra",
            "subgenus Hippotigris",
    };
    String[] moscowAnswer = {
            "by population within city limits worldwide",
            "the area of the capital more than doubled",
            "the Ostankino Tower",
            "ninth",
            "the European continent",
    };

    private DataSet moscowDataSet() {
        DataSet data = new DataSet();
        String[] questions = {
                "Moscow is broadly defined by the 11th largest by which metric?",
                "What happened to Moscow on its territorial expansion on July 1, 2012?",
                "Moscow is home to which tallest free standing structure in Europe?",
                "What is the rank of Moscow in the list of most expensive cities in the world?",
                "Moscow is the largest city entirely on which continent?"
        };

        String[] answers = {
                "the Ostankino Tower",
                "the area of the capital more than doubled",
                "ninth",
                "the European continent",
                "by population within city limits worldwide"
        };
        data.article = "Moscow is a major political, economic, cultural, and scientific center of Russia and Eastern Europe, as well as the largest city entirely on the European continent. By broader definitions Moscow is among the world's largest cities, being the 14th largest metro area, the 18th largest agglomeration, the 15th largest urban area, and the 11th largest by population within city limits worldwide. According to Forbes 2013, Moscow has been ranked as the ninth most expensive city in the world by Mercer and has one of the world's largest urban economies, being ranked as an alpha global city according to the Globalization and World Cities Research Network, and is also one of the fastest growing tourist destinations in the world according to the MasterCard Global Destination Cities Index. Moscow is the northernmost and coldest megacity and metropolis on Earth. It is home to the Ostankino Tower, the tallest free standing structure in Europe; the Federation Tower, the tallest skyscraper in Europe; and the Moscow International Business Center. By its territorial expansion on July 1, 2012 southwest into the Moscow Oblast, the area of the capital more than doubled, going from 1,091 to 2,511 square kilometers (421 to 970 sq mi), and it gained an additional population of 233,000 people.";
        data.providedAnswers = Arrays.asList(answers);
        data.questions = Arrays.asList(questions);

        return data;
    }

    private DataSet jobsDataSet() {
        DataSet data = new DataSet();
        String[] questions = {
                "What Jobs decided to do in 1974?",
                "What did Jobs told a reporter about taking LSD? ",
                "Who where his two closest friends?",
                "Where was Jobs adopted at birth?",
                "Who was Job's musical idol?"
        };

        String[] answers = {
                "San Francisco",
                "older engineering student (and Homestead High alumnus) Wozniak and his girlfriend, the artistically inclined and countercultural Homestead High junior Chrisann Brennan",
                "Bob Dylan",
                "to travel through India",
                "“one of the two or three most important things” he did in his life"
        };
        data.article = "Jobs's countercultural lifestyle and philosophy was a product of the time and place of his upbringing. Jobs was adopted at birth in San Francisco, and raised in a hotbed of counterculture, the San Francisco Bay Area during the 1960s. As a senior at Homestead High School in Cupertino, California, his two closest friends were the older engineering student (and Homestead High alumnus) Wozniak and his girlfriend, the artistically inclined and countercultural Homestead High junior Chrisann Brennan. Jobs and Wozniak bonded over their mutual fascination with Job's musical idol Bob Dylan, discussing his lyrics and collecting bootleg reel-to-reel tapes of Dylan's concerts. Jobs later dated Joan Baez who notably had a prior relationship with Dylan. Jobs briefly attended Reed College in 1972 before dropping out. He then decided to travel through India in 1974 and to study Zen Buddhism. Job's declassified FBI report says an acquaintance knew that Jobs used illegal drugs in college including marijuana and LSD. Jobs told a reporter once that taking LSD was “one of the two or three most important things” he did in his life.";
        data.providedAnswers = Arrays.asList(answers);
        data.questions = Arrays.asList(questions);

        return data;
    }

    private DataSet zebraDataSet() {
        DataSet data = new DataSet();
        String[] questions = {
                "Which Zebras are endangered?",
                "What is the aim of the Quagga Project?",
                "Which animals are some of their closest relatives?",
                "Which are the three species of zebras?",
                "Which subgenus do the plains zebra and the mountain zebra belong to?"};

        String[] answers = {
                "subgenus Hippotigris",
                "the plains zebra, the Grévy's zebra and the mountain zebra",
                "horses and donkeys",
                "aims to breed zebras that are phenotypically similar to the quagga",
                "Grévy's zebra and the mountain zebra"
        };
        data.article = "Zebras are several species of African equids (horse family) united by their distinctive black and white stripes. Their stripes come in different patterns, unique to each individual. They are generally social animals that live in small harems to large herds. Unlike their closest relatives, horses and donkeys, zebras have never been truly domesticated. There are three species of zebras: the plains zebra, the Grévy's zebra and the mountain zebra. The plains zebra and the mountain zebra belong to the subgenus Hippotigris, but Grévy's zebra is the sole species of subgenus Dolichohippus. The latter resembles an ass, to which it is closely related, while the former two are more horse-like. All three belong to the genus Equus, along with other living equids. The unique stripes of zebras make them one of the animals most familiar to people. They occur in a variety of habitats, such as grasslands, savannas, woodlands, thorny scrublands, mountains, and coastal hills. However, various anthropogenic factors have had a severe impact on zebra populations, in particular hunting for skins and habitat destruction. Grévy's zebra and the mountain zebra are endangered. While plains zebras are much more plentiful, one subspecies, the quagga, became extinct in the late 19th century – though there is currently a plan, called the Quagga Project, that aims to breed zebras that are phenotypically similar to the quagga in a process called breeding back.";
        data.providedAnswers = Arrays.asList(answers);
        data.questions = Arrays.asList(questions);

        return data;
    }

    @Test
    public void testFindAnswers() {
        try {
            AnswerMatcher.findAnswers(null);
        } catch (AssertionError error) {
            assertTrue("Null check", error.getMessage().equals("Input data can't be null"));
        }

        assertEquals("Zebra dataset", Arrays.asList(zebraAnswer), AnswerMatcher.findAnswers(zebraDataSet()));
        assertEquals("Moscow dataset", Arrays.asList(moscowAnswer), AnswerMatcher.findAnswers(moscowDataSet()));
        assertEquals("Jobs dataset", Arrays.asList(jobsAnswer), AnswerMatcher.findAnswers(jobsDataSet()));
    }

    @Test
    public void testPreProcess() throws Exception {
        assertEquals("zebras endangered", AnswerMatcher.preProcess(zebraDataSet().questions.get(0)));
        assertEquals("hello darkness my old friend!! yous", AnswerMatcher.preProcess("Hello      Darkness, my; Old friend!!? how is yous"));
    }
}