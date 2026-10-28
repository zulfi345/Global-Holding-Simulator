package id.holding.sim.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class PremiRisikoTest {
    @Test fun rasioDasar() {
        assertEquals(1.5, PremiRisiko.der(150.0, 100.0), 1e-9)
        assertEquals(4.0, PremiRisiko.interestCoverage(400.0, 100.0), 1e-9)
        assertEquals(2.0, PremiRisiko.currentRatio(200.0, 100.0), 1e-9)
    }

    @Test fun ekuitasNolAtauNegatifDerTakHingga() {
        assertEquals(Double.POSITIVE_INFINITY, PremiRisiko.der(100.0, 0.0), 0.0)
        assertEquals(Double.POSITIVE_INFINITY, PremiRisiko.der(100.0, -50.0), 0.0)
    }

    @Test fun tanpaBungaAtauLiabilitasLancarDianggapAman() {
        assertEquals(Double.POSITIVE_INFINITY, PremiRisiko.interestCoverage(100.0, 0.0), 0.0)
        assertEquals(Double.POSITIVE_INFINITY, PremiRisiko.currentRatio(100.0, 0.0), 0.0)
    }

    @Test fun poinDerPadaBatas() {
        assertEquals(0, PremiRisiko.poinDer(1.0))
        assertEquals(1, PremiRisiko.poinDer(2.0))
        assertEquals(2, PremiRisiko.poinDer(3.0))
        assertEquals(3, PremiRisiko.poinDer(3.01))
        assertEquals(3, PremiRisiko.poinDer(Double.POSITIVE_INFINITY))
    }

    @Test fun poinCoveragePadaBatas() {
        assertEquals(0, PremiRisiko.poinCoverage(5.0))
        assertEquals(1, PremiRisiko.poinCoverage(3.0))
        assertEquals(2, PremiRisiko.poinCoverage(1.5))
        assertEquals(3, PremiRisiko.poinCoverage(1.49))
        assertEquals(3, PremiRisiko.poinCoverage(-2.0))
    }

    @Test fun poinCurrentRatioPadaBatas() {
        assertEquals(0, PremiRisiko.poinCurrentRatio(1.5))
        assertEquals(1, PremiRisiko.poinCurrentRatio(1.0))
        assertEquals(2, PremiRisiko.poinCurrentRatio(0.8))
        assertEquals(3, PremiRisiko.poinCurrentRatio(0.79))
    }

    @Test fun perusahaanSehatPremiNol() {
        assertEquals(0.0, PremiRisiko.premiPct(0.5, 8.0, 2.0), 1e-9)
    }

    @Test fun perusahaanSangatBerisikoPremiMaksimum() {
        assertEquals(6.75, PremiRisiko.premiPct(5.0, 0.5, 0.5), 1e-9)
    }

    @Test fun premiCampuran() {
        // der 1.5 -> 1, icr 4 -> 1, cr 0.9 -> 2 = 4 poin x 0.75 = 3.0
        assertEquals(3.0, PremiRisiko.premiPct(1.5, 4.0, 0.9), 1e-9)
    }

    @Test fun peringkatDariTotalPoin() {
        assertEquals(PeringkatKredit.A, PremiRisiko.peringkat(1))
        assertEquals(PeringkatKredit.B, PremiRisiko.peringkat(3))
        assertEquals(PeringkatKredit.C, PremiRisiko.peringkat(6))
        assertEquals(PeringkatKredit.D, PremiRisiko.peringkat(7))
    }
}
