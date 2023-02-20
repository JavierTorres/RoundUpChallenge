package com.javiertorres.StarlingAPIChallenge.domain.feature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavingsGoalFeature {

    public static final String PUT_SAVINGS_GOAL_JSON_RESPONSE = """
        {
            "savingsGoalUid": "23c1004b-4cd1-4b7a-83fc-80b0ab3adfd7",
            "success": true
        }
        """.stripIndent();

    public static final String GET_SAVINGS_GOALS_JSON_RESPONSE = """
        {
          "savingsGoalList": [
            {
              "savingsGoalUid": "77887788-7788-7788-7788-778877887788",
              "name": "Trip to Paris",
              "target": {
                "currency": "GBP",
                "minorUnits": 123456
              },
              "totalSaved": {
                "currency": "GBP",
                "minorUnits": 123456
              },
              "savedPercentage": 100
            }
          ]
        }
        """.stripIndent();

    public static final String getPutSavingsGoalJsonRequest(String name, Long minorUnits) {

        return """
            {
              "name": "{name}",
              "currency": "GBP",
              "target": {
                "currency": "GBP",
                "minorUnits": {minorUnits}
              }
            }
            """
            .replace("{name}", name)
            .replace("{minorUnits}", minorUnits.toString())
            .stripIndent();
    }
}
