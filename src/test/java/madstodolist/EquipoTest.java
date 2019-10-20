package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoTest {
    @Autowired
    private EquipoRepository equipoRepository;

    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Proyecto Cobalto");
        assertThat(equipo.getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    @Transactional
    public void grabarEquipo() {
        // GIVEN
        Equipo equipo = new Equipo("Proyecto Cobalto");

        // WHEN
        equipoRepository.save(equipo);

        // THEN
        assertThat(equipo.getId()).isNotNull();
    }
}