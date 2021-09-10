package team.peiYangCoders.PeiYangResourceManagement.bean;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ResourceRepository resourceRepo;
    private final ItemRepository itemRepo;
    private final StudentCertificateRepository certificateRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Faker faker = new Faker();


    @Autowired
    public Runner(UserRepository userRepo,
                  ResourceRepository resourceRepo,
                  ItemRepository itemRepo,
                  StudentCertificateRepository certificateRepo,
                  BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.resourceRepo = resourceRepo;
        this.itemRepo = itemRepo;
        this.certificateRepo = certificateRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Override
    public void run(String... args) throws IOException {

        logger.info("initializing database...");

        // fake data
        for(int i = 0; i < 100; i++){
            User u = fakeUser();
            if(Math.random() > 0.5){
                StudentCertificate certificate = fakeStudentCertificate(u);
                certificate.setUsed(true);
                u.setStudentId(certificate.getStudentId());
                u.setStudentCertified(true);
                certificateRepo.save(certificate);
            }
            for(int j = 0; j < (int) (Math.random() * 10); j++){
                Resource resource = fakeResource(u);
                resource.setVerified(Math.random() > 0.5);
                if(resource.isVerified()) resource.setAccepted(Math.random() > 0.5);
                if(resource.isVerified() && resource.isAccepted())
                    resource.setReleased(Math.random() > 0.5);
                if(resource.isReleased()){
                    for(int k = 0; k < (int) (Math.random() * 5); k++){
                        Item item = fakeItem(resource);
                        itemRepo.save(item);
                    }
                }
                resourceRepo.save(resource);
            }
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            userRepo.save(u);
        }

        User shiYu = fakeUser();
        shiYu.setUserName("ShiYu");
        shiYu.setPhone("18802201509");
        shiYu.setPassword(passwordEncoder.encode("123456"));
        userRepo.save(shiYu);

        User peng1 = fakeUser();
        peng1.setUserName("PengPeng");
        peng1.setUserTag("admin");
        peng1.setPhone("19960741644");
        peng1.setPassword(passwordEncoder.encode("123456"));
        userRepo.save(peng1);

        User peng2 = fakeUser();
        peng2.setUserName("PengPeng");
        peng2.setUserTag("ordinary");
        peng2.setPhone("12345678910");
        peng2.setPassword(passwordEncoder.encode("123456"));
        userRepo.save(peng2);

        User peng3 = fakeUser();
        peng3.setUserName("PengPeng");
        peng3.setUserTag("ordinary");
        peng3.setPhone("10987654321");
        peng3.setPassword(passwordEncoder.encode("123456"));
        userRepo.save(peng3);

        User elzat0 = fakeUser();
        elzat0.setUserName("Elzat");
        elzat0.setPhone("17627669320");
        elzat0.setUserTag("ordinary");
        elzat0.setPassword(passwordEncoder.encode("300059"));
        userRepo.save(elzat0);

        User elzat1 = fakeUser();
        elzat1.setUserName("Elzat");
        elzat1.setPhone("18799081038");
        elzat1.setUserTag("admin");
        elzat1.setPassword(passwordEncoder.encode("300059"));
        userRepo.save(elzat1);


        logger.info("database has initialized");
    }

    private User fakeUser(){
        return new User(
                null, faker.phoneNumber().cellPhone(), null,
                faker.number().digits(10), faker.number().digits(10), faker.name().firstName(),
                faker.number().digits(12), "", false, "ordinary"
        );
    }

    private StudentCertificate fakeStudentCertificate(User user){
        return new StudentCertificate(
                null, faker.number().digits(10), user.getUserName(),
                faker.number().digits(12), false
        );
    }

    private Resource fakeResource(User user){
        return new Resource(
                null, faker.book().title(), faker.book().genre(), "book",
                false, false, false, "", user.getPhone()
        );
    }

    private Item fakeItem(Resource resource){

        String name = resource.getResourceName();
        String description = resource.getDescription();
        String type = Math.random() > 0.5 ? "sell" : "lease";
        int count = (int) (Math.random() * 10);
        LocalDateTime onTime = LocalDateTime.now().minusDays((long) (Math.random() * 30))
                .minusMinutes((long) (Math.random() * 60))
                .minusHours((long) (Math.random() * 60));
        boolean needs2Pay = Math.random() > 0.5;
        int fee = needs2Pay ? (int) (Math.random() * 200) : 0;
        String feeUnit = needs2Pay ? (type.equals("lease") ? "$/h" : "$") : "";
        LocalDateTime startsAt = type.equals("lease") ?
                onTime.plusMinutes((long) (Math.random() * 60))
                .plusHours((long) (Math.random() * 60)) : null;
        LocalDateTime endsAt = type.equals("lease") ?
                startsAt.plusHours((long) (Math.random() * 60))
                        .plusMinutes((long) (Math.random() * 60)) : null;
        int campus = (int) (Math.random() + Math.random());
        String resourceCode = resource.getResourceCode();
        String ownerPhone = resource.getOwnerPhone();


        return new Item(
                null, name, description, type, count, onTime, needs2Pay, fee,
                feeUnit, startsAt, endsAt, false, campus, resourceCode, ownerPhone
        );
    }
}
