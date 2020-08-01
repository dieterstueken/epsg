package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  27.02.14 13:26
 * modified by: $Author$
 * modified on: $Date$
 */
abstract public class OutputAppender implements Appendable {

    @Override
    abstract public OutputAppender append(CharSequence csq);

    public OutputAppender append(Number n) {
        if(n==null)
            append("null");
        else
            append(n.toString());

        return this;
    }

    @Override
    abstract public OutputAppender append(CharSequence csq, int start, int end);

    @Override
    abstract public OutputAppender append(char c);

    abstract public OutputAppender nl();

    public OutputAppender sp() {
        return append(' ');
    }

    abstract public OutputAppender indent(int shift);

    abstract public OutputAppender remark(String remarks);

    public OutputAppender format(String format, Object ... args) {
        String result = String.format(format, args);
        append(result);
        return this;
    }

    public void addCommentLine(String text) {

        if(isNullOrEmpty(text))
            return;

        int i = text.indexOf(". ");
        if(i>=0) {
            ++i;
            addCommentLine(text.substring(0, i));

            while(i<text.length() && text.charAt(i)==' ')
                ++i;

            addCommentLine(text.substring(i));

            return;
        }

        i = text.indexOf('¶');
        if(i>=0) {
            addCommentLine(text.substring(0, i));
            if(i<text.length())
                addCommentLine(text.substring(i+1));
            return;
        }

        // skip leading tabs
        for(i=0; i<text.length(); ++i)
            if(text.charAt(i)!='\t')
                break;

        if(i>=text.length())
            return; // empty

        i = text.indexOf(i, '\t');
        if(i>0) {
            addCommentLine(text.substring(0, i));
            addCommentLine(text.substring(i));

            return;
        }

        nl().append("//     ").append(text);
    }

    public OutputAppender addComment(String header, String text) {

        if(isNullOrEmpty(text))
            return this;

        nl().append("// ").append(header);
        nl().append("//    ");
        addCommentLine(text);
        nl().append("//    ");

        return this;
    }

    public OutputAppender appendNumber(int number) {
        return appendCode(Integer.toString(number));
    }


    public OutputAppender appendCode(Object code) {
        return appendCode("", code);
    }

    public OutputAppender appendCode(String prefix, Object code) {
        if(code==null)
            append("null");
        else
            append(prefix).append(code.toString());

        return this;
    }

    public OutputAppender quote(String text) {
        if(text==null) {
            append("null");
            return this;
        }

        append('"');

        for(int i=0; i<text.length(); ++i) {
            char ch = text.charAt(i);
            if(ch=='"' || ch=='\\')
                append('\\');
            append(ch);
        }

        append('"');

        return this;
    }

    public static boolean isNullOrEmpty(String text) {
        return text==null || text.trim().isEmpty();
    }
}
