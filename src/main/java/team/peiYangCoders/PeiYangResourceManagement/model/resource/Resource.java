package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
            name = "resource_code",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private String resourceCode;

    /**
    * resource name:
    *   any resource has to have a name;
    *   provided by user.
    * */
    @Column(
            name = "resource_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String resourceName;

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
    @Column(
            name = "resource_tag",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String resourceTag;

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



    private String ownerPhone;
}
