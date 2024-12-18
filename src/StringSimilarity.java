public class StringSimilarity {

    public static boolean isCloseEnough(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();

        // Create a 2D array to store the edit distances
        int[][] dp = new int[m + 1][n + 1];

        // Initialize base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Deletions to match an empty string
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Insertions to match an empty string
        }

        // Fill in the dp table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // No operation needed
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],     // Deletion
                            Math.min(dp[i][j - 1],     // Insertion
                                    dp[i - 1][j - 1]));// Substitution
                }
            }
        }

        return dp[m][n] <= 2; // Edit distance between str1 and str2

    }
}
