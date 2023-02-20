package com.javiertorres.StarlingAPIChallenge.domain.feature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopUpFeature {
    public static String PUT_TOP_UP_REQUEST = """
        {
          "amount": {
            "currency": "GBP",
            "minorUnits": 123456
          }
        }
        """.stripIndent();

    public static String PUT_TOP_UP_RESPONSE = """
        {
          "success": true
        }
        """.stripIndent();
}
