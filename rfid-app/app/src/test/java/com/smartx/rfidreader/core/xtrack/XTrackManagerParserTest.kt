package com.smartx.shopeerecebimento.core.xtrack

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

    @Test
    fun parseDevicesResponse_parsesJsonArrayAndCaseInsensitiveKeys() {
        val json = """
            {"data":[
              {"id":"6","active":"1","uid":"COLETOR_ALMOX","name":"{ PREFIX :[ 123 , 456 ], EPC_LEN:[]}"},
              {"ID":"7","ACTIVE":"0","UID":"INATIVO","NAME":""}
            ]}
        """.trimIndent()

        val devices = xtrack_manager.parseDevicesResponse(json)

        assertEquals(2, devices.size)
        assertEquals("COLETOR_ALMOX", devices[0].uid)
        assertEquals("{ PREFIX :[ 123 , 456 ], EPC_LEN:[]}", devices[0].name)
        assertEquals("0", devices[1].active)
    }

    @Test
    fun parseDevicesResponse_acceptsSingleJsonDataObjectAndXml() {
        val json = """{"data":{"ID":"1","ACTIVE":"1","UID":"Unit","NAME":"payload"}}"""
        val xml = """
            <root><data><ID>2</ID><ACTIVE>1</ACTIVE><UID>XML-DEVICE</UID><NAME>xml payload</NAME></data></root>
        """.trimIndent()

        assertEquals("Unit", xtrack_manager.parseDevicesResponse(json).single().uid)
        assertEquals("XML-DEVICE", xtrack_manager.parseDevicesResponse(xml).single().uid)
    }

    @Test
    fun parseDeviceConfig_acceptsSpacesAndQuotedKeysAndValues() {
        val config = xtrack_manager.parseDeviceConfig("""{ "PREFIX" : [ "abc" , 'def' ], 'EPC_LEN'=[ 24 , 32 ] }""")

        assertEquals(listOf("ABC", "DEF"), config.prefixes)
        assertEquals(listOf(24, 32), config.epcLengths)
        assertTrue(config.errors.isEmpty())
    }

    @Test
    fun parseDeviceConfig_distinguishesMissingAndEmptyFields() {
        val absent = xtrack_manager.parseDeviceConfig("{ OTHER:[1] }")
        val empty = xtrack_manager.parseDeviceConfig("{ PREFIX:[], EPC_LEN:[] }")

        assertEquals(null, absent.prefixes)
        assertEquals(null, absent.epcLengths)
        assertEquals(emptyList<String>(), empty.prefixes)
        assertEquals(emptyList<Int>(), empty.epcLengths)
        assertTrue(empty.errors.isEmpty())
    }

    @Test
    fun parseDeviceConfig_keepsValidLengthsAndReportsInvalidValues() {
        val config = xtrack_manager.parseDeviceConfig("{ PREFIX:[xyz], EPC_LEN:[24, nope, 0, 32] }")

        assertEquals(listOf("XYZ"), config.prefixes)
        assertEquals(listOf(24, 32), config.epcLengths)
        assertEquals(2, config.errors.size)
    }

    @Test
    fun parseDeviceConfig_allInvalidLengthsDoNotClearExistingSetting() {
        val config = xtrack_manager.parseDeviceConfig("{ EPC_LEN:[nope, 0] }")

        assertEquals(null, config.epcLengths)
        assertEquals(2, config.errors.size)
    }

    @Test
    fun findSelectedDevice_matchesUidAndUsesDefaultWhenSelectionMissing() {
        val devices = listOf(
            XTrackDevice("1", "1", "Other", "{}"),
            XTrackDevice("2", "1", "  coletor_almox ", "{}")
        )

        assertEquals("Other", xtrack_manager.findSelectedDevice(devices, " other ")?.uid)
        assertEquals("  coletor_almox ", xtrack_manager.findSelectedDevice(devices, "")?.uid)
    }
}
