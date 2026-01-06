package com.zzzjian.music.domain.model

object MockData {
    val favoriteSongs = listOf(
        Song(0, "喵喵之歌", "哈基米", "萌宠专辑", 180000, "", null, "https://regengbaike.com/uploads/20230222/1bff61de34bdc7bf40c6278b2848fbcf.jpg"),
        Song(1, "午后阳光", "橘猫", "懒洋洋", 200000, "", null, "https://regengbaike.com/uploads/20230222/1bff61de34bdc7bf40c6278b2848fbcf.jpg"),
        Song(2, "呼噜声ASMR", "英短", "治愈系", 150000, "", null, "https://regengbaike.com/uploads/20230222/1bff61de34bdc7bf40c6278b2848fbcf.jpg")
    )

    val recentSongs = listOf(
        Song(3, "追逐激光笔", "小黑", "运动时间", 120000, "", null, "https://regengbaike.com/uploads/20230222/1bff61de34bdc7bf40c6278b2848fbcf.jpg"),
        Song(4, "罐头开盖声", "吃货喵", "美食", 5000, "", null, "https://regengbaike.com/uploads/20230222/1bff61de34bdc7bf40c6278b2848fbcf.jpg")
    )

    val downloadedSongs = listOf(
        Song(5, "踩奶进行曲", "奶牛猫", "按摩大师", 240000, "", null, "https://images.unsplash.com/photo-1529778873920-4da4926a7071?w=128&h=128&fit=crop")
    )

    val allSongs = favoriteSongs + recentSongs + downloadedSongs + listOf(
        Song(6, "猫薄荷狂欢", "美短", "派对", 160000, "", null, "https://images.unsplash.com/photo-1513245543132-31f507417b26?w=128&h=128&fit=crop"),
        Song(7, "抓板练习曲", "暹罗", "运动", 140000, "", null, "https://images.unsplash.com/photo-1501820488136-72669149e0d4?w=128&h=128&fit=crop"),
        Song(8, "深夜跑酷", "跑酷喵", "夜猫子", 180000, "", null, "https://images.unsplash.com/photo-1494256997604-09951f52bd4d?w=128&h=128&fit=crop")
    )
}