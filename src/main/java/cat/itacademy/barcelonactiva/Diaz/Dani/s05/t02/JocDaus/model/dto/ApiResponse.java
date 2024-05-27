package cat.itacademy.barcelonactiva.Diaz.Dani.s05.t02.JocDaus.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse <T> {

    private String message;
    private T data;
}
