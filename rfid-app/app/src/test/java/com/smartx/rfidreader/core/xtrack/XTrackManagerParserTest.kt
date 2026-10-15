package com.smartx.rfidreader.core.xtrack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class XTrackManagerParserTest {

    @Test
    fun parseLocationsXml_parsesGetLocationResponse() {
        val xml = """
            <DocumentElement>
                <data>
                    <ID>1</ID>
                    <NAME>Tecnologia</NAME>
                    <CODE>Tecnologia</CODE>
                </data>
                <data>
                    <ID>2</ID>
                    <NAME>Area Comercial</NAME>
                    <CODE>Area Comercial</CODE>
                </data>
                <data>
                    <ID>8</ID>
                    <NAME>Estoque</NAME>
                    <CODE>Estoque</CODE>
                </data>
            </DocumentElement>
        """.trimIndent()

        val locations = xtrack_manager.parseLocationsXml(xml)

        assertEquals(3, locations.size)
        assertEquals("1", locations[0].id)
        assertEquals("Tecnologia", locations[0].name)
        assertEquals("2", locations[1].id)
        assertEquals("Area Comercial", locations[1].name)
        assertEquals("8", locations[2].id)
        assertEquals("Estoque", locations[2].name)
    }

    @Test
    fun parseLocationsXml_ignoresRowsWithoutIdAndName() {
        val xml = """
            <DocumentElement>
                <data>
                    <CODE>Sem campos válidos</CODE>
                </data>
                <data>
                    <ID>9</ID>
                    <NAME>Deposito</NAME>
                </data>
            </DocumentElement>
        """.trimIndent()

        val locations = xtrack_manager.parseLocationsXml(xml)

        assertEquals(1, locations.size)
        assertTrue(locations.any { it.id == "9" && it.name == "Deposito" })
    }

    @Test
    fun parseObjectInfosXml_mapsRfidIdCodeToDescriptionAndObjectIdCode() {
        val xml = """
            <DocumentElement>
                <data>
                    <DESCRIPTION>Camisa Polo Preta</DESCRIPTION>
                        <IDCODE>5003</IDCODE>
                    <IDENTIFICATION>
                        <TYPE>BARCODE</TYPE>
                        <IDCODE>5003</IDCODE>
                    </IDENTIFICATION>
                    <IDENTIFICATION>
                        <TYPE>RFID</TYPE>
                        <IDCODE>000000000000000000000004</IDCODE>
                    </IDENTIFICATION>
                </data>
                <data>
                        <IDCODE>#000000012</IDCODE>
                    <DESCRIPTION>Teste dia 05/10</DESCRIPTION>
                    <IDENTIFICATION>
                        <TYPE>RFID</TYPE>
                        <IDCODE>000000000000000000425855</IDCODE>
                    </IDENTIFICATION>
                </data>
            </DocumentElement>
        """.trimIndent()

        val infos = xtrack_manager.parseObjectInfosXml(xml)

        assertEquals(2, infos.size)
        assertEquals("Camisa Polo Preta", infos["000000000000000000000004"]?.description)
        assertEquals("5003", infos["000000000000000000000004"]?.objectIdCode)
        assertEquals("Teste dia 05/10", infos["000000000000000000425855"]?.description)
        assertEquals("#000000012", infos["000000000000000000425855"]?.objectIdCode)
    }
}
