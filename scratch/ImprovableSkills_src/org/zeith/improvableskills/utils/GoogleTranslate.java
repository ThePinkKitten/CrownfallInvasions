/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

public final class GoogleTranslate {
    private static final String GOOGLE_TRANSLATE_URL = "http://translate.google.com/translate_a/single";

    private GoogleTranslate() {
    }

    public static String getDisplayLanguage(String languageCode) {
        return new Locale(languageCode).getDisplayLanguage();
    }

    private static String generateURL(String sourceLanguage, String targetLanguage, String text) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(text, "UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_TRANSLATE_URL);
        sb.append("?client=webapp");
        sb.append("&hl=en");
        sb.append("&sl=");
        sb.append(sourceLanguage);
        sb.append("&tl=");
        sb.append(targetLanguage);
        sb.append("&q=");
        sb.append(encoded);
        sb.append("&multires=1");
        sb.append("&otf=0");
        sb.append("&pc=0");
        sb.append("&trs=1");
        sb.append("&ssel=0");
        sb.append("&tsel=0");
        sb.append("&kc=1");
        sb.append("&dt=t");
        sb.append("&ie=UTF-8");
        sb.append("&oe=UTF-8");
        sb.append("&tk=");
        sb.append(GoogleTranslate.generateToken(text));
        return sb.toString();
    }

    public static String detectLanguage(String text) throws IOException {
        String urlText = GoogleTranslate.generateURL("auto", "en", text);
        URL url = new URL(urlText);
        String rawData = GoogleTranslate.urlToText(url);
        return GoogleTranslate.findLanguage(rawData);
    }

    public static String translate(String text) throws IOException {
        return GoogleTranslate.translate(Locale.getDefault().getLanguage(), text);
    }

    public static String translate(String targetLanguage, String text) throws IOException {
        return GoogleTranslate.translate("auto", targetLanguage, text);
    }

    public static String translate(String sourceLanguage, String targetLanguage, String text) throws IOException {
        String urlText = GoogleTranslate.generateURL(sourceLanguage, targetLanguage, text);
        URL url = new URL(urlText);
        String rawData = GoogleTranslate.urlToText(url);
        if (rawData == null) {
            return null;
        }
        String[] raw = rawData.split("\"");
        if (raw.length < 2) {
            return null;
        }
        return raw[1];
    }

    private static String urlToText(URL url) throws IOException {
        int ch;
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");
        InputStreamReader r = new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8"));
        StringBuilder buf = new StringBuilder();
        while ((ch = ((Reader)r).read()) >= 0) {
            buf.append((char)ch);
        }
        return buf.toString();
    }

    private static String findLanguage(String rawData) {
        int i = 0;
        while (i + 5 < rawData.length()) {
            boolean dashDetected;
            boolean bl = dashDetected = rawData.charAt(i + 4) == '-';
            if (rawData.charAt(i) == ',' && rawData.charAt(i + 1) == '\"' && (rawData.charAt(i + 4) == '\"' && rawData.charAt(i + 5) == ',' || dashDetected)) {
                if (dashDetected) {
                    int lastQuote = rawData.substring(i + 2).indexOf(34);
                    if (lastQuote > 0) {
                        return rawData.substring(i + 2, i + 2 + lastQuote);
                    }
                } else {
                    String possible = rawData.substring(i + 2, i + 4);
                    if (GoogleTranslate.containsLettersOnly(possible)) {
                        return possible;
                    }
                }
            }
            ++i;
        }
        return null;
    }

    private static boolean containsLettersOnly(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isLetter(text.charAt(i))) continue;
            return false;
        }
        return true;
    }

    private static int[] TKK() {
        return new int[]{406398, 2087938574};
    }

    private static int shr32(int x, int bits) {
        if (x < 0) {
            long x_l = 0xFFFFFFFFL + (long)x + 1L;
            return (int)(x_l >> bits);
        }
        return x >> bits;
    }

    private static int RL(int a, String b) {
        for (int c = 0; c < b.length() - 2; c += 3) {
            int d = b.charAt(c + 2);
            d = d >= 65 ? d - 87 : d - 48;
            d = b.charAt(c + 1) == '+' ? GoogleTranslate.shr32(a, d) : a << d;
            a = b.charAt(c) == '+' ? a + (d & 0xFFFFFFFF) : a ^ d;
        }
        return a;
    }

    private static String generateToken(String text) {
        int[] tkk = GoogleTranslate.TKK();
        int b = tkk[0];
        int e = 0;
        ArrayList<Integer> d = new ArrayList<Integer>();
        for (int f = 0; f < text.length(); ++f) {
            int g = text.charAt(f);
            if (128 > g) {
                d.add(e++, g);
                continue;
            }
            if (2048 > g) {
                d.add(e++, g >> 6 | 0xC0);
            } else if (55296 == (g & 0xFC00) && f + 1 < text.length() && 56320 == (text.charAt(f + 1) & 0xFC00)) {
                g = 65536 + ((g & 0x3FF) << 10) + (text.charAt(++f) & 0x3FF);
                d.add(e++, g >> 18 | 0xF0);
                d.add(e++, g >> 12 & 0x3F | 0x80);
            } else {
                d.add(e++, g >> 12 | 0xE0);
                d.add(e++, g >> 6 & 0x3F | 0x80);
            }
            d.add(e++, g & 0x3F | 0x80);
        }
        int a_i = b;
        for (e = 0; e < d.size(); ++e) {
            a_i += ((Integer)d.get(e)).intValue();
            a_i = GoogleTranslate.RL(a_i, "+-a^+6");
        }
        a_i = GoogleTranslate.RL(a_i, "+-3^+b+-f");
        long a_l = 0 > (a_i ^= tkk[1]) ? 0x80000000L + (long)(a_i & Integer.MAX_VALUE) : (long)a_i;
        a_l = (long)((double)a_l % Math.pow(10.0, 6.0));
        return String.format(Locale.US, "%d.%d", a_l, a_l ^ (long)b);
    }
}

