package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
* Resource class:
*   resource can be anything but it has belong to a user;
*   so there is a many to one relationship between resources and users;
*   a resource can appear in different orders;
*   so there is a one to many relationship between resources and orders.
* */

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "Resource")
@Data
@Table(
        name = "resources"
)
public class Resource {

    /**
    *
    * */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            name = "code",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID code;

    /**
    * resource name:
    *   any resource has to have a name;
    *   provided by user.
    * */
    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    /*
    * resource description:
    *   users can describe the resources;
    *   but don`t have to.
    * */
    @Column(
            name = "description",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String description;

    /*
    * */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tag",
            columnDefinition = "TEXT",
            nullable = false
    )
    private ResourceTag tag;

    /**
     * verified by administrator
     * */
    @Column(
            name = "verified",
            nullable = false
    )
    private boolean verified = false;

    /**/
    @Column(
            name = "released",
            nullable = false
    )
    private boolean released = false;

    @Column(
            name = "accepted",
            nullable = false
    )
    private boolean accepted = false;

    /*
    * resource image url:
    *   users can upload one image for each resource;
    *   but don`t have to.
    * */
    @URL
    @Column(
            name = "image_url",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String imageUrl;

    /*
    * user id:
    *   every resource has to belong to a user;
    *   user stored in the users table;
    *   so there is foreign key user_id referenced user.id.
    * */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "resource_user_fk")
    )
    private User owner;


    @OneToMany
    private List<Item> items;



    public static Resource getFromBody(Body.ResourceInfos resourceInfos, User owner){
        Resource resource = new Resource();
        resource.setName(resourceInfos.getName());
        resource.setDescription(resourceInfos.getDescription());
        resource.setTag(ResourceTag.valueOf(resourceInfos.getTag()));
        resource.setImageUrl(resourceInfos.getImageUrl());
        resource.setVerified(false);
        resource.setReleased(false);
        resource.setOwner(owner);
        return resource;
    }

    public static Body.ResourceInfos toBody(Resource resource){
        Body.ResourceInfos infos = new Body.ResourceInfos();
        infos.setCode(resource.getCode().toString());
        infos.setName(resource.getName());
        infos.setDescription(resource.getDescription());
        infos.setTag(resource.getTag().toString());
        infos.setImageUrl(resource.getImageUrl());
        infos.setVerified(resource.isVerified());
        infos.setReleased(infos.isReleased());
        infos.setOwnerPhone(resource.getOwner().getPhone());
        return infos;
    }
}
