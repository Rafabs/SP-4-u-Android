import os

enc = 'utf-8'

fragment_linhas = """<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Consulte as linhas do Sistema Metropolitano"
        android:textSize="18sp"
        android:textStyle="bold"
        android:padding="16dp"/>
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_linhas"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingLeft="12dp"
        android:paddingRight="12dp"
        android:paddingBottom="12dp"
        android:clipToPadding="false"/>
</LinearLayout>"""

item_linha = """<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="4dp"
    android:layout_marginBottom="4dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="2dp">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="14dp">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical">
            <View
                android:id="@+id/view_cor"
                android:layout_width="14dp"
                android:layout_height="14dp"
                android:background="@drawable/circle_color"/>
            <TextView
                android:id="@+id/tv_nome"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_marginLeft="12dp"
                android:textSize="15sp"
                android:textStyle="bold"/>
            <TextView
                android:id="@+id/tv_hex"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="monospace"
                android:textSize="12sp"
                android:background="@drawable/badge_background_code"
                android:paddingLeft="8dp"
                android:paddingRight="8dp"
                android:paddingTop="3dp"
                android:paddingBottom="3dp"/>
        </LinearLayout>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:layout_marginTop="8dp">
            <TextView
                android:id="@+id/tv_empresa"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_marginLeft="26dp"
                android:textSize="12sp"/>
            <TextView
                android:id="@+id/tv_status"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="11sp"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                android:paddingTop="4dp"
                android:paddingBottom="4dp"/>
        </LinearLayout>
    </LinearLayout>
</androidx.cardview.widget.CardView>"""

with open(r'app\src\main\res\layout\fragment_linhas.xml', 'w', encoding=enc) as f:
    f.write(fragment_linhas)
print('fragment_linhas.xml OK')

with open(r'app\src\main\res\layout\item_linha.xml', 'w', encoding=enc) as f:
    f.write(item_linha)
print('item_linha.xml OK')