package com.nchl.authorization_server.Utility;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URI;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("util")
public class Utilities {

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'s', 't', 'b', 'j', 'c', 'S', 'B', 'u', 'W', '2', 'O', 'B', 'o', 'r', 'S', 'u'};
    private static String CIPHER = "12345667ABCDEG";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public String encodePassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

   /* public String generateUserPwd() {
        String SpecialChars = "!@#$%^*";
        String password = RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.random(1, SpecialChars) + RandomStringUtils.randomNumeric(8);
        return password.toUpperCase();
    }

    public String generateUserMpin() {
        String mpin = RandomStringUtils.randomNumeric(6);
        return mpin.toUpperCase();
    }

    public String lpad(String str, int padlength, String padvalue) {
        return StringUtils.leftPad(str, padlength, padvalue);
    }

    public String rpad(String str, int padlength, String padvalue) {
        return StringUtils.rightPad(str, padlength, padvalue);
    }*/

    public String truncate(String str, int length) {
        if (str != null) {
            return str.substring(0, Math.min(length, str.length()));
        } else {
            return "";
        }
    }

    public String convertIsoAmount(String amount) {
        if (amount.startsWith("-")) {
            return "(" + amount.substring(1).replaceFirst("^0+(?!$)", "") + ")";
        } else {
            return amount.substring(1).replaceFirst("^0+(?!$)", "");
        }
    }

    public boolean isNumberWith2Decimals(BigDecimal value) {
        try {
            String tranAmt = value.toPlainString();
            return tranAmt.matches("^\\s*(?=.*[1-9])\\d*(?:\\.\\d{1,2})?\\s*$");
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }

    }

    public boolean isStrongPassword(String password) {
        boolean result = false;
        final Pattern pattern;
        final Matcher matcher;

        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        result = matcher.matches();
        return result;
    }

    public <T> T nvl(T arg0, T arg1) {
        return (arg0 == null || arg0.equals("null")) ? arg1 : arg0;
    }

    public String getCurrentDateTime(String dateTimeFormat) {
        String result = "";
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        Date date = new Date();
        result = dateFormat.format(date);
        return result;
    }

    public String convertDateFormat(String dateStr, String dateFormat, String newDateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(dateStr);
            sdf = new SimpleDateFormat(newDateFormat);
            dateStr = sdf.format(date);
        } catch (Exception ex) {
            System.out.println("Date Convert Exception: " + ex);
        }
        return dateStr;
    }

    public Date getDate(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(dateStr);
            return date;
        } catch (Exception ex) {
            throw new RuntimeException("Invalid Date Format");
        }
    }

    public String dateToString(Date inDate, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            String dateString = sdf.format(inDate);
            return dateString;
        } catch (Exception ex) {
            throw new RuntimeException("Invalid Date Format");
        }
    }

    public Date addDaysToDate(int noOfDays) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, noOfDays);
        dt = c.getTime();
        return dt;
    }

 /*   public String generatePassword() {
        List<CharacterRule> rules = Arrays.asList(
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1));

        PasswordGenerator generator = new PasswordGenerator();

        // Generated password is 12 characters long, which complies with policy
        String password = generator.generatePassword(12, rules);

        return password;
    }

    public boolean validatePasswordStrength(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // length between 8 and 20 characters
                new LengthRule(8, 20),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            System.out.println("Password is valid");
            return true;
        } else {
            System.out.println("Invalid password:");
            for (String msg : validator.getMessages(result)) {
                System.out.println(msg);
            }
        }
        return false;
    }*/

    public boolean validateMpin(String mpin) {
        Pattern VALID_MPIN_NO_REGEX =
                Pattern.compile("^[0-9]{6}$");

        Matcher matcher = VALID_MPIN_NO_REGEX.matcher(mpin);
        return matcher.find();

    }


    public boolean validateEmailId(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }



    public String jasyptEncrypt(String text) {
        try {
            System.out.println("jasyptEncrypt: " + text);
            //BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            //encryptor.setSaltGenerator(new ZeroSaltGenerator());
            encryptor.setPoolSize(4);
            encryptor.setPassword(CIPHER);
            return encryptor.encrypt(text);
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public String jasyptDecrypt(String text) {
        try {

            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(CIPHER);
            return textEncryptor.decrypt(text);
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public String jasyptEncrypt(String text, String cipher) {
        try {
            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            encryptor.setSaltGenerator(new ZeroSaltGenerator());
            encryptor.setPoolSize(4);
            encryptor.setPasswordCharArray(cipher.toCharArray());
            return encryptor.encrypt(text);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
    }

    public String jasyptDecrypt(String text, String cipher) {
        try {

            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            encryptor.setSaltGenerator(new ZeroSaltGenerator());
            encryptor.setPoolSize(4);
            encryptor.setPasswordCharArray(cipher.toCharArray());
            return encryptor.decrypt(text);
        } catch (Exception ex) {
            return null;
        }
    }

    public String encrypt(String data) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encVal);
        } catch (Exception ex) {
            return null;
        }
    }

/*
	public static void main(String[] args) {
		Utilities util1 = new Utilities();


		String password = "123456";
		String userCipher = "CIPS";
		String encPassword = util1.jasyptEncrypt(password, "TKCUL"+userCipher+"HIEFX");
		System.out.println("Encrypt : ENC("+ encPassword +") , Decrypt : "+ util1.jasyptDecrypt(encPassword, "TKCUL"+userCipher+"HIEFX"));
		//Encrypt :LqOyFteVApI=, Decrypt : 123456


		//System.out.println(util1.isNumberWith2Decimals("45"));
	}*/

    public String decrypt(String encryptedData) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
    }

/*    public boolean isValidCreditCard(String str) {

        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String generateStr(int i) {
        return i < 0 ? "" : generateStr((i / 26) - 1) + (char) (65 + i % 26);
    }

    public XMLGregorianCalendar createXMLGregorianCalendar(Date currentDateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(currentDateTime);

        XMLGregorianCalendar createDate;
        try {
            createDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }

        return createDate;
    }

    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }


	*//*public static void main(String args[]){
		 //Utilities util =new Utilities();
	//System.out.println(util.generateStr(18277));
		final List<Integer> list = new ArrayList<>();

		list.add(1);
		list.add(2);
		list.add(3);

		//list.remove(2);
		System.out.println(list);

	 }*//*


    public boolean isThisDateValid(String dateToValidate, String dateFromat) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String generateRequestIdentifier() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getCurrentDateTime("yyyyMMddHHmmssSSS"));// 17
        stringBuilder.append(RandomStringUtils.randomAlphanumeric(20 - stringBuilder.length()));// 10
        return stringBuilder.toString();
    }

    public String getResponseStatusBasedOnCreditStatus(RefundResponse refundResponse){

        Predicate<RefundResponse> creditSuccess = item -> TxnStatus.SUCCESS.getCode().equals(item.getCreditStatus()) || TxnStatus.DEFER.getCode().equals(item.getCreditStatus()) || TxnStatus.TIMEOUT.getCode().equals(item.getCreditStatus());
        Boolean result = Stream.of(refundResponse).anyMatch(creditSuccess);
        if(result){
            return TxnStatus.SUCCESS.getCode();
        }else {
            return refundResponse.getCreditStatus();
        }
    }*/

    public static final BigDecimal roundAmount(BigDecimal amount) {
        BigDecimal bd = new BigDecimal(amount.doubleValue());
        bd.setScale(2, BigDecimal.ROUND_HALF_UP); // this does change bd
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static URI getUri(String uriString) {
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(uriString).build();
        return uriBuilder.toUri();
    }
}
