package pl.dev4lazy.shit_exchange.utils;

import com.ibm.icu.text.CharsetDetector;

public class TextEncodingTool {

      public static String getCharsetFromText(String text  ) {
          CharsetDetector detector = new CharsetDetector();
          detector.setText( text.getBytes() );
          return detector.detect().getName();
      }

}

