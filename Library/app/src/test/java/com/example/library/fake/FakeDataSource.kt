package com.example.library.fake

import com.example.library.data.api.ItemDto
import com.example.library.data.api.VolumeDto
import com.example.library.data.api.VolumeImageDto
import com.example.library.data.api.VolumeInfoDto

object FakeDataSource {
    val item= ItemDto(
        listOf(
        VolumeDto("1",
            VolumeInfoDto("android_1",listOf("1_1","1_2"),"publisher1",
                "1111","description1", VolumeImageDto(),false)
        ),
        VolumeDto("2",
            VolumeInfoDto("android_2",listOf("2_1","2_2"),"publisher2",
                "0202","description2", VolumeImageDto(),false)
        ),
        VolumeDto("3",
            VolumeInfoDto("android_3",listOf("3_1","3_2"),"publisher3",
                "0303","description3", VolumeImageDto(),false)
        ),
    ),0)
}