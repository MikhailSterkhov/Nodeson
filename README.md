<div align="center">

![logo](/assets/Nodeson.png)

# ‹ NODESON ›

[![MIT License](https://img.shields.io/github/license/pl3xgaming/Purpur?&logo=github)](LICENSE)

</div>

---

## ABOUT


Maximum speed for parsing and converting Java Objects to JSON format:
* Use various parsing options
* Multi-thread parsing and conversion
* Flexibility

The qualities listed above fully describe the performance of this library.

---

## HOW TO USE?


First, let's define what kind of parser we need.

There are only two types of parsing in this library:
* Common
```java
NodesonParser common = Nodeson.common();
```
* Parallel (Multi-Threaded)

```java
NodesonParser parallel = Nodeson.parallel();
```


Then we can already reproduce the necessary parsing algorithms.

Let's try to parse an object to JSON and back, measuring the speed:

**Common:** (189ms)
```java
String json = common.parse(testObject); // 178ms
        
TestObject converted = common.convert(json, TestObject.class); // 11ms
```

**Parallel:** (20ms)
```java
String json = parallel.parse(testObject); // 18ms
        
TestObject converted = parallel.convert(json, TestObject.class); // 2ms
```

---

## PLEASE, SUPPORT ME

By clicking on this link, you can support me as a
developer and motivate me to develop new open-source projects!

<a href="https://www.buymeacoffee.com/itzstonlex" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>
