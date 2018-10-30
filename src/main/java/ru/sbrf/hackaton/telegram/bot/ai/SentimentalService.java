package ru.sbrf.hackaton.telegram.bot.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.client.ClientBot;
import ru.sbrf.hackaton.telegram.bot.model.Sentiment;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
public class SentimentalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBot.class);
    private static FilteredClassifier m_classifier;
    private static String m_algorithm;

    @PostConstruct
    public void init() throws Exception {
        LOGGER.debug("Loading model...");
        final String model = "src/main/resources/sentimentalModelSbrf.model";
        m_classifier = (FilteredClassifier) SerializationHelper.read(model);
        m_algorithm = m_classifier.getClassifier().getClass().getSimpleName();
        LOGGER.debug("Model loaded");
        LOGGER.debug("Model trained algorithm: " + m_algorithm);
    }

    private Instances makeInstance(final String text) {
        final ArrayList<Attribute> fvWekaAttributes = new ArrayList<Attribute>(2);
        final Attribute attrText = new Attribute("text", (ArrayList<String>) null);
        fvWekaAttributes.add(attrText);

        final ArrayList<String> fvClassVal = new ArrayList<String>(3);

        fvClassVal.add(Sentiment.BAD.name().toLowerCase());
        fvClassVal.add(Sentiment.NEUTRAL.name().toLowerCase());
        fvClassVal.add(Sentiment.GOOD.name().toLowerCase());
        final Attribute attrClass = new Attribute("@@class@@", fvClassVal);
        fvWekaAttributes.add(attrClass);

        final Instances instances = new Instances("Rel", fvWekaAttributes, 0);
        instances.setClassIndex(instances.numAttributes() - 1);

        final Instance instance = new DenseInstance(2);
        instance.setValue(attrText, text);
        instances.add(instance);

        return instances;
    }

    public Sentiment getSentiment(final String message) {

        final Instances instances = makeInstance(message);

        final Instance in = instances.instance(0);

        final double[] ps;
        try {
            ps = m_classifier.distributionForInstance(in);
            SentimentalResult[] sentimentalResult = IntStream.range(0, ps.length)
                    .mapToObj(i -> new SentimentalResult(in.classAttribute().value(i), ps[i]))
                    .toArray(SentimentalResult[]::new);
            LOGGER.debug(Arrays.toString(sentimentalResult));
            OptionalDouble max = DoubleStream.of(ps).max();
            int index = DoubleStream.of(ps).boxed().collect(toList()).indexOf(max.getAsDouble());
            return Sentiment.valueOf(in.classAttribute().value(index).toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return Sentiment.NEUTRAL;
        }
    }

    public String getAlgorithm() {
        return m_algorithm;
    }

    public class SentimentalResult {
        public final double score;
        public final String clazz;

        public SentimentalResult(final String clazz, final double score) {
            this.score = score;
            this.clazz = clazz;
        }

        public String toString() {
            return this.clazz + " -> " + this.score;
        }
    }

}
