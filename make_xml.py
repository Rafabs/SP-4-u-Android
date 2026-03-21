content = """<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp">
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            app:cardCornerRadius="12dp"
            app:cardElevation="4dp">
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Sampa 4u"
                    android:textSize="24sp"
                    android:textStyle="bold"
                    android:layout_marginBottom="12dp"/>
                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="O Sampa 4u e um ecossistema de visualizacao de dados focado na malha metroferroviaria e de onibus urbano de Sao Paulo."
                    android:textSize="15sp"
                    android:lineSpacingMultiplier="1.5"
                    android:layout_marginBottom="20dp"/>
                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:background="@drawable/badge_background_blue"
                    android:paddingLeft="12dp"
                    android:paddingRight="12dp"
                    android:paddingTop="6dp"
                    android:paddingBottom="6dp"
                    android:layout_marginBottom="8dp">
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="VERSAO" android:textSize="11sp" android:textStyle="bold" android:textColor="#FFFFFF"/>
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text=" | " android:textColor="#AAFFFFFF" android:textSize="11sp"/>
                    <TextView android:id="@+id/tv_versao" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="..." android:textSize="11sp" android:textColor="#FFFFFF"/>
                </LinearLayout>
                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:background="@drawable/badge_background_green"
                    android:paddingLeft="12dp"
                    android:paddingRight="12dp"
                    android:paddingTop="6dp"
                    android:paddingBottom="6dp"
                    android:layout_marginBottom="8dp">
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="ATUALIZACAO" android:textSize="11sp" android:textStyle="bold" android:textColor="#FFFFFF"/>
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text=" | " android:textColor="#AAFFFFFF" android:textSize="11sp"/>
                    <TextView android:id="@+id/tv_data" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="..." android:textSize="11sp" android:textColor="#FFFFFF"/>
                </LinearLayout>
                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:background="@drawable/badge_background_orange"
                    android:paddingLeft="12dp"
                    android:paddingRight="12dp"
                    android:paddingTop="6dp"
                    android:paddingBottom="6dp">
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="LICENCA" android:textSize="11sp" android:textStyle="bold" android:textColor="#FFFFFF"/>
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text=" | " android:textColor="#AAFFFFFF" android:textSize="11sp"/>
                    <TextView android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="MIT" android:textSize="11sp" android:textColor="#FFFFFF"/>
                </LinearLayout>
            </LinearLayout>
        </androidx.cardview.widget.CardView>
    </LinearLayout>
</ScrollView>"""

with open(r'app\src\main\res\layout\fragment_home.xml', 'w', encoding='utf-8') as f:
    f.write(content)
print('OK')