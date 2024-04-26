package org.example.desafiotools.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "transacao")
@Table(schema = "desafio_tecnico")
public class Transacao {


}
