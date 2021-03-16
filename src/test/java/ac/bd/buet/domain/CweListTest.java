package ac.bd.buet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ac.bd.buet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CweListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CweList.class);
        CweList cweList1 = new CweList();
        cweList1.setId(1L);
        CweList cweList2 = new CweList();
        cweList2.setId(cweList1.getId());
        assertThat(cweList1).isEqualTo(cweList2);
        cweList2.setId(2L);
        assertThat(cweList1).isNotEqualTo(cweList2);
        cweList1.setId(null);
        assertThat(cweList1).isNotEqualTo(cweList2);
    }
}
