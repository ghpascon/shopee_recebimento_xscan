package com.smartx.shopeerecebimento.core.registry

import com.smartx.shopeerecebimento.core.reader.IRfidReader
import com.smartx.shopeerecebimento.readers.c72.C72Reader
import com.smartx.shopeerecebimento.readers.cfh301.CfH301Reader
import com.smartx.shopeerecebimento.readers.ih25.IH25Reader
import com.smartx.shopeerecebimento.readers.tsl1128.Tsl1128Reader
import com.smartx.shopeerecebimento.readers.x714.X714Reader
import com.smartx.shopeerecebimento.readers.xr2.XR2Reader
import com.smartx.shopeerecebimento.readers.zebra.ZebraReader

/**
 * Registro central de todos os leitores RFID suportados.
 *
 * Para adicionar um novo leitor:
 *   1. Implemente [IRfidReader] para o modelo
 *   2. Adicione a instância a [availableReaders]
 *   Pronto — a UI e o restante do app detectam automaticamente.
 */
object ReaderRegistry {

    val availableReaders: List<IRfidReader> by lazy {
        listOf(
            C72Reader(),
            CfH301Reader(),
            IH25Reader(),
            ZebraReader(),
            Tsl1128Reader(),
            X714Reader(),
            XR2Reader()
        )
    }

    fun findById(readerId: String): IRfidReader? =
        availableReaders.firstOrNull { it.readerId == readerId }
}
