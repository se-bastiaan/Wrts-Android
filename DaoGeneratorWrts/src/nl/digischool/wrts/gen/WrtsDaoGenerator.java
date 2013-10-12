package nl.digischool.wrts.gen;

import de.greenrobot.daogenerator.*;

import java.net.URL;

/**
 * Generates entities and DAOs for the Wrts project.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Sébastiaan Versteeg
 */
public class WrtsDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "nl.digischool.wrts.database");

        addWordListWord(schema);

        URL location = WrtsDaoGenerator.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location.getFile());

        new DaoGenerator().generateAll(schema, "src");
    }

    private static void addWordListWord(Schema schema) {
        Entity list = schema.addEntity("WordList");
        list.addIdProperty();
        list.addIntProperty("result_count");
        list.addStringProperty("title").notNull();
        list.addStringProperty("created_on").notNull();
        list.addStringProperty("updated_on").notNull();
        list.addStringProperty("lang_a");
        list.addStringProperty("lang_b");
        list.addStringProperty("lang_c");
        list.addStringProperty("lang_d");
        list.addStringProperty("lang_e");
        list.addStringProperty("lang_f");
        list.addStringProperty("lang_g");
        list.addStringProperty("lang_h");
        list.addStringProperty("lang_i");
        list.addStringProperty("lang_j");
        list.addBooleanProperty("shared");

        Entity word = schema.addEntity("Word");
        word.addIdProperty();
        Property listId = word.addLongProperty("list_id").notNull().getProperty();
        word.addStringProperty("word_a");
        word.addStringProperty("word_b");
        word.addStringProperty("word_c");
        word.addStringProperty("word_d");
        word.addStringProperty("word_e");
        word.addStringProperty("word_f");
        word.addStringProperty("word_g");
        word.addStringProperty("word_h");
        word.addStringProperty("word_i");
        word.addStringProperty("word_j");

        word.addToOne(list, listId);
        ToMany listToWords = list.addToMany(word, listId);
        listToWords.setName("words");

        Entity changes = schema.addEntity("Changes");
        changes.addIdProperty();
        changes.addIntProperty("type").notNull();
        Property listIdchange = changes.addLongProperty("list_id").notNull().getProperty();
        changes.addToOne(list, listIdchange);
    }

}
