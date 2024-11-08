package com.fitfinance.app.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitfinance.app.domain.model.InvestmentType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "investment")
@Parcelize
data class InvestmentEntity(
    @PrimaryKey val id: Long,
    var name: String,
    var price: Double,
    var type: InvestmentType,
    var quantity: Int,
    var startDate: String,
    var endDate: String?
) : Parcelable
