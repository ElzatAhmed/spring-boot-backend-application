package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPage {

    private int pageNum;

    private int pageSize;

    private Sort.Direction direction = Sort.Direction.DESC;

    private String sortBy = "onTime";

    public ItemPage(int pageNum, int pageSize, String sortBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
    }
}
