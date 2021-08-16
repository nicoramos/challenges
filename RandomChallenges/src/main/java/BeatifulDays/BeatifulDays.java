package BeatifulDays;

public class BeatifulDays {
    private BeatifulDays() {
    }

    public static int beatifullDays(int i, int j, int k) {
        if (i < 0 || j < 0 || j < i) {
            return -1;
        }

        int count = 0;
        for (int l = i; l <= j; l++) {
            int diff = l - reverse(l);

            if (diff % k == 0) {
                count++;
            }
        }

        return count;
    }

    private static int reverse(int i) {
        StringBuilder str = new StringBuilder(String.valueOf(i));
        return Integer.parseInt(str.reverse().toString());
    }
}
