package game.subsidiaries.audio;

import java.io.FilterInputStream;
import java.io.InputStream;

/**
 * Created by lazarus on 06/04/17.
 */
public class SoundFilter extends FilterInputStream {

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected SoundFilter(InputStream in) {
        super(in);
    }

    /***
     *
     * @param buffer
     * @param position
     * @return
     */
    public short getSample(byte[] buffer, int position)
    {
        return (short) (((buffer[position+1] & 0xff) << 8) | (buffer[position] & 0xff));
    }

    /***
     *
     * @param buffer
     * @param position
     * @param sample
     */
    public void setSample(byte[] buffer, int position, short sample)
    {
        buffer[position] = (byte)(sample & 0xFF);
        buffer[position+1] = (byte)((sample >> 8) & 0xFF);
    }
}
