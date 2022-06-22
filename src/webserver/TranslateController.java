import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import spark.Request;
import spark.Response;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TranslateController{

    public static String post(Request req, Response res){

        // Capture body param
        String original_text = req.body();
        System.out.println();
        //JSONObject body_json = new JSONObject(body);
        //String original_text = (String) body_json.get("text");

        // New Console Stream

        PrintStream previousConsole = System.out;
        ByteArrayOutputStream newConsole = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newConsole));

        // Create lexer from body param

        HTMLLexer lexer = new HTMLLexer(CharStreams.fromString(original_text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTMLParser parser = new HTMLParser(tokens);

        ParseTree tree = parser.htmlDocument();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new Translator(), tree);
        previousConsole.println(newConsole.toString());
        System.setOut(previousConsole);
        String translated_text =  newConsole.toString();
        res.status(200);
        return translated_text;
    }
}