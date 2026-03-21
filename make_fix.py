enc = 'utf-8'

# 1. circle_color.xml
with open(r'app\src\main\res\drawable\circle_color.xml', 'w', encoding=enc) as f:
    f.write("""<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#000000"/>
</shape>""")
print('circle_color.xml OK')

# 2. badge_status_green.xml
with open(r'app\src\main\res\drawable\badge_status_green.xml', 'w', encoding=enc) as f:
    f.write("""<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#00550E"/>
    <corners android:radius="4dp"/>
</shape>""")
print('badge_status_green.xml OK')