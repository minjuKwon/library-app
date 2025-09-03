package com.example.library.data.fake

import com.example.library.data.api.ItemDto
import com.example.library.data.api.VolumeDto
import com.example.library.data.api.VolumeImageDto
import com.example.library.data.api.VolumeInfoDto
import kotlin.math.min

object FakeDataSource {
    private val item= ItemDto(
        listOf(
            VolumeDto("1",
                VolumeInfoDto("android_1",listOf("1_1","1_2"),"publisher1",
                    "0101","description1", VolumeImageDto(),false)
            ),
            VolumeDto("2",
                VolumeInfoDto("android_2",listOf("2_1","2_2"),"publisher2",
                    "0202","description2", VolumeImageDto(),false)
            ),
            VolumeDto("3",
                VolumeInfoDto("android_3",listOf("3_1","3_2"),"publisher3",
                    "0303","description3", VolumeImageDto(),false)
            ),
            VolumeDto("4",
                VolumeInfoDto("android_4",listOf("4_1","4_2"),"publisher4",
                    "0404","description4", VolumeImageDto(),false)
            ),
            VolumeDto("5",
                VolumeInfoDto("android_5",listOf("5_1","5_2"),"publisher5",
                    "0505","description5", VolumeImageDto(),false)
            ),
            VolumeDto("6",
                VolumeInfoDto("android_6",listOf("6_1","6_2"),"publisher6",
                    "0606","description6", VolumeImageDto(),false)
            ),
            VolumeDto("7",
                VolumeInfoDto("android_7",listOf("7_1","7_2"),"publisher7",
                    "0707","description7", VolumeImageDto(),false)
            ),
            VolumeDto("8",
                VolumeInfoDto("android_8",listOf("8_1","8_2"),"publisher8",
                    "0808","description8", VolumeImageDto(),false)
            ),
            VolumeDto("9",
                VolumeInfoDto("android_9",listOf("9_1","9_2"),"publisher9",
                    "0909","description9", VolumeImageDto(),false)
            ),
            VolumeDto("10",
                VolumeInfoDto("android_10",listOf("10_1","10_2"),"publisher10",
                    "1010","description10", VolumeImageDto(),false)
            ),
            VolumeDto("11",
                VolumeInfoDto("android_11",listOf("11_1","11_2"),"publisher11",
                    "1111","description11", VolumeImageDto(),false)
            ),
            VolumeDto("12",
                VolumeInfoDto("android_12",listOf("12_1","12_2"),"publisher12",
                    "1212","description12", VolumeImageDto(),false)
            ),
            VolumeDto("13",
                VolumeInfoDto("android_13",listOf("13_1","13_2"),"publisher3",
                    "1313","description13", VolumeImageDto(),false)
            ),
            VolumeDto("14",
                VolumeInfoDto("android_14",listOf("14_1","14_2"),"publisher14",
                    "1414","description14", VolumeImageDto(),false)
            ),
            VolumeDto("15",
                VolumeInfoDto("android_15",listOf("15_1","15_2"),"publisher15",
                    "1515","description15", VolumeImageDto(),false)
            )
    ),15)

    fun getListRange(limit:Int, offset:Int): ItemDto {
        return ItemDto(item.book.subList(offset, min(offset+limit, item.totalCount)), item.totalCount)
    }
}