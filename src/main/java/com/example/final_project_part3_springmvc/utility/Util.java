package com.example.final_project_part3_springmvc.utility;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
@Component
public class Util {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    public boolean validate(Objects entity) {

        Set<ConstraintViolation<Objects>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            System.out.println("Invalid user data found:");
            for (ConstraintViolation<Objects> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
    }

    public static byte[] saveImage(String path) {
        File file1 = new File(path);
        if (isJPEG(new File(path)) && file1.length() < 300000) {
            File file = new File(path);
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            byte[] bytes = new byte[(int) file.length()];
            try {
                is.read(bytes);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return bytes;
        }

        return null;
    }

    private static Boolean isJPEG(File filename) {
        boolean flag = false;
        DataInputStream ins;
        try {
            ins = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            flag = ins.readInt() == 0xffd8ffe0;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return flag;
    }

    public  static int getHourDifferenceBetweenDates(LocalDateTime date1, LocalDateTime date2) {
        long hoursDifference = ChronoUnit.HOURS.between(date1, date2);
        return (int) hoursDifference;
    }
}
