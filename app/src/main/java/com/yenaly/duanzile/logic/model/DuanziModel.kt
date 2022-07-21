package com.yenaly.duanzile.logic.model

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 21:06
 */
data class DuanziModel(
    override val msg: String,
    override val code: Int,
    val data: DuanziListModel.Datum
) : IDuanzileModel
