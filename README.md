# JsonDemo

![image](https://github.com/Jimison-TW/AndroidDemo03-OpenDataAnalysis/blob/master/device-2018-07-27-152922.png?raw=true)

## 開發版本
Andorid 3.1.2 </br>
SdkVersion 27 </br>
minSdkVersion 15 </br>
targetSdkVersion 27 </br>

## 學習重點
1. 使用Okhttp來抓取Open data網站的資料
2. 使用傳統的Json解析方式來解析Json物件
3. 透過google開發的套件Gson來解析Json資料

## Gson如何解析
透過一個自定義的物件類別，作為解析後承接的物件格式
```java=
public class JsonData {
        @SerializedName("StationName")
        private String station;
        @SerializedName("AvaliableBikeCount")
        private int bikeCount;
        @SerializedName("AvaliableSpaceCount")
        private int spaceCount;

        public String getStation() {
            return station;
        }

        public int getBikeCount() {
            return bikeCount;
        }

        public int getSpaceCount() {
            return spaceCount;
        }
    }
```

建立一個Gson物件，並透過fromJson()方法，丟入Json字串與Type泛型類別
```java=
final List<JsonData> jsonData = new Gson().
    fromJson(str, new TypeToken<List<JsonData>>() {}.getType());
```
