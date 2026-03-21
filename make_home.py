enc = 'utf-8'

content = """<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
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
                    android:text="O Sampa 4u e um ecossistema de visualizacao de dados focado na malha metroferroviaria e de onibus urbano de Sao Paulo. O projeto transforma dados operacionais e coordenadas em interfaces graficas intuitivas, facilitando a compreensao da mobilidade urbana na capital paulista."
                    android:textSize="15sp"
                    android:lineSpacingMultiplier="1.5"
                    android:layout_marginBottom="20dp"/>
                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:background="@drawable/badge_background_orange"
                    android:paddingLeft="12dp"
                    android:paddingRight="12dp"
                    android:paddingTop="6dp"
                    android:paddingBottom="6dp">
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="LICENCA"
                        android:textSize="11sp"
                        android:textStyle="bold"
                        android:textColor="#FFFFFF"/>
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text=" | "
                        android:textColor="#AAFFFFFF"
                        android:textSize="11sp"/>
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="MIT"
                        android:textSize="11sp"
                        android:textColor="#FFFFFF"/>
                </LinearLayout>
            </LinearLayout>
        </androidx.cardview.widget.CardView>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Consulte as linhas do Sistema Metropolitano"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="12dp"/>
<androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recycler_linhas"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:nestedScrollingEnabled="false"
            android:overScrollMode="never"/>
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_marginTop="16dp"
            android:background="@drawable/card_ccm_background"
            android:padding="16dp">
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="CCM | Centro de Controle Metroferroviario"
                android:textSize="16sp"
                android:textStyle="bold"
                android:layout_marginBottom="10dp"/>
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Os status operacionais exibidos acima sao extraidos da API publica do Centro de Controle Metroferroviario (CCM) da ARTESP. Os dados sao coletados automaticamente a cada 3 horas via GitHub Actions e cobrem todas as linhas do sistema metroferroviario paulista, incluindo Metro, CPTM e Concessionarias."
                android:textSize="14sp"
                android:lineSpacingMultiplier="1.5"/>
        </LinearLayout>
</androidx.core.widget.NestedScrollView>"""

with open(r'app\src\main\res\drawable\card_ccm_background.xml', 'w', encoding=enc) as f:
    f.write("""<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="#0D000000"/>
            <corners android:radius="4dp"/>
        </shape>
    </item>
    <item android:left="0dp">
        <shape android:shape="rectangle">
            <solid android:color="#00000000"/>
            <stroke android:width="5dp" android:color="#D40000"/>
            <corners android:radius="4dp"/>
        </shape>
    </item>
</layer-list>""")
print('card_ccm_background.xml OK')