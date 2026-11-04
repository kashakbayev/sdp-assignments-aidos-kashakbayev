
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.Segment;
import model.Server;
import model.Workstation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VisitorsTest {
    private Network network;
    private Segment dataCenter;

    @BeforeEach
    void setup() {
        Segment office = new Segment("office", List.of(
                new Server("web", "10.0.1.1", 2, 5),
                new Workstation("laptop", "10.0.1.2", 1, 3),
                new IoTDevice("camera", "10.0.1.3", 3, 2)
        ));

        dataCenter = new Segment("dc", List.of(
                new Server("db", "10.0.2.1", 4, 5),
                new Firewall("fw", "10.0.2.2", 0, 4)
        ));

        network = new Network(
                "all", List.of(office, dataCenter)
        );
    }

    @Test
    void calculatesNetworkRiskByHand() {
        assertEquals(
                195,
                network.accept(new RiskScoreVisitor()).intValue()
        );
    }

    @Test
    void calculatesOnlySegmentRisk() {
        assertEquals(
                85,
                dataCenter.accept(new RiskScoreVisitor()).intValue()
        );
    }

    @Test
    void accumulatesStatisticsAcrossTree() {
        SecurityStatistics result =
                network.accept(new SecurityStatisticsVisitor());

        assertEquals(2, result.servers());
        assertEquals(1, result.workstations());
        assertEquals(1, result.iotDevices());
        assertEquals(1, result.firewalls());
        assertEquals(5, result.totalDevices());
        assertEquals(10, result.totalVulnerabilities());
    }

    @Test
    void accumulatesOnlySelectedSubtree() {
        SecurityStatistics result =
                dataCenter.accept(new SecurityStatisticsVisitor());

        assertEquals(2, result.totalDevices());
        assertEquals(4, result.totalVulnerabilities());
    }

    @Test
    void filtersVulnerableDevices() {
        List<String> ids = network
                .accept(new VulnerableDeviceVisitor())
                .stream()
                .map(d -> d.id())
                .toList();

        assertEquals(List.of("web", "camera", "db"), ids);
    }

    @Test
    void filterRespectsSubtree() {
        List<String> ids = dataCenter
                .accept(new VulnerableDeviceVisitor())
                .stream()
                .map(d -> d.id())
                .toList();

        assertEquals(List.of("db"), ids);
    }

    @Test
    void exportsNestedJson() {
        String json =
                network.accept(new JsonExportVisitor());

        assertTrue(json.startsWith(
                "{\"type\":\"network\",\"id\":\"all\",\"segments\":["
        ));

        assertTrue(json.contains(
                "\"type\":\"segment\",\"id\":\"dc\",\"devices\":["
        ));

        assertTrue(json.contains(
                "\"type\":\"firewall\",\"id\":\"fw\""
        ));

        assertTrue(json.endsWith("]}"));
    }

    @Test
    void escapesJsonControlCharacters() {
        Network quoted = new Network(
                "n\"\\\n", List.of()
        );

        String json =
                quoted.accept(new JsonExportVisitor());

        assertTrue(json.contains("n\\\"\\\\\\n"));
    }

    @Test
    void rejectsInvalidDevices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Server("s", "10.0.0.1", -1, 5)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Firewall("f", "10.0.0.2", 0, 6)
        );
    }

    @Test
    void copiesCompositeChildren() {
        var original = new java.util.ArrayList<Segment>();
        original.add(dataCenter);

        Network copy = new Network("safe", original);
        original.clear();

        assertEquals(1, copy.segments().size());

        assertThrows(
                UnsupportedOperationException.class,
                () -> copy.segments().clear()
        );
    }
}
