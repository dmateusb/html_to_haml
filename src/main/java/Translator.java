import java.util.ArrayList;

public class Translator extends HTMLParserBaseListener {
    int indent = -1;
    ArrayList<String> attributes;

    private String indentation(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            s.append("  ");
        }
        return s.toString();
    }


    @Override
    public void enterHtmlElement(HTMLParser.HtmlElementContext ctx) {
        attributes = new ArrayList<>();
        super.enterHtmlElement(ctx);
        indent++;
        String indentation = indentation();
        String tag = ctx.TAG_NAME().get(0).toString();
        System.out.print( indentation + "%" + tag + " " );
        if ( ctx.htmlAttribute().size() == 0 &&
            ctx.htmlContent().htmlChardata().get(0).HTML_TEXT() == null ){
            System.out.println("");
        }
    }

    @Override
    public void enterHtmlComment(HTMLParser.HtmlCommentContext ctx) {
        super.enterHtmlComment(ctx);
        indent++;
        System.out.println(indentation() + ctx.HTML_COMMENT().toString().replace("<!--", "/ ").replace("-->"," "));

    }

    @Override
    public void exitHtmlComment(HTMLParser.HtmlCommentContext ctx) {
        super.exitHtmlComment(ctx);
        indent--;
    }

    @Override
    public void exitHtmlElement(HTMLParser.HtmlElementContext ctx) {

        super.exitHtmlElement(ctx);
        indent--;
    }

    @Override
    public void enterHtmlChardata(HTMLParser.HtmlChardataContext ctx) {
        super.enterHtmlChardata(ctx);

        if(ctx.HTML_TEXT() != null) {
            System.out.println(ctx.HTML_TEXT());
        }
    }

    @Override
    public void enterHtmlAttribute(HTMLParser.HtmlAttributeContext ctx) {
        super.enterHtmlAttribute(ctx);
        String text = "";
        if(ctx.TAG_NAME().toString().equals("id")) {
            System.out.print("#" + ctx.ATTVALUE_VALUE().toString().replaceAll("\"", "").replaceAll("\\s+","") + " ");
        }else if(ctx.TAG_NAME().toString().equals("class")){
            System.out.print("." + ctx.ATTVALUE_VALUE().toString().replaceAll("\"", "").replaceAll("\\s+","") + " ");
        }else{
            text = ":" + ctx.TAG_NAME().toString() + "=>" + ctx.ATTVALUE_VALUE().toString() ;
            attributes.add(text);
        }

    }

    @Override
    public void enterHtmlContent(HTMLParser.HtmlContentContext ctx) {
        super.enterHtmlContent(ctx);
        String attributes_str = "";

        for (String attribute: attributes) {
            attributes_str += attribute + ",";
        }
        if (attributes_str.length() > 2){
            String middle_text = attributes_str.substring(0, attributes_str.length() - 1);
            System.out.println("{" + middle_text + "}");
        }

    }
}
