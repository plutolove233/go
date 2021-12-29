package message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class MyInputStream extends ObjectInputStream {
    public MyInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected void readStreamHeader() throws IOException {
        //super.reset();
    }
}
