package com.grupo30.capitalhumano.evaluaciones.dto;

import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatoGestionDTO {

	    private Integer nroCandidato;
	        private String apellido;
		    private String nombre;
		        private TipoDocumento tipoDocumento;
			    private String nroDocumento;
			        private String email;

}
