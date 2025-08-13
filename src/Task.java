import com.sun.xml.internal.ws.developer.Serialization;

import java.util.Date;

@Serialization
public class Task {
    private String tittle;
    private String description;
    private Date  endDate;

    public Task(String tittle, String description, Date endDate){
        this.tittle = tittle;
        this.description = description;
        this.endDate = endDate;
    }
}
