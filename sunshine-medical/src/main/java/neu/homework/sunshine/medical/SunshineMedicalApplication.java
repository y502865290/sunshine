package neu.homework.sunshine.medical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients
public class SunshineMedicalApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(SunshineMedicalApplication.class,args);
    }
}
