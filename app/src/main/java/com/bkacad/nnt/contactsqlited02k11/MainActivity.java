package com.bkacad.nnt.contactsqlited02k11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bkacad.nnt.contactsqlited02k11.adapter.ContactAdapter;
import com.bkacad.nnt.contactsqlited02k11.database.DAO;
import com.bkacad.nnt.contactsqlited02k11.database.DBHelper;
import com.bkacad.nnt.contactsqlited02k11.model.Contact;
import com.bkacad.nnt.contactsqlited02k11.model.ContactDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtName, edtPhone;
    private Button btnInsert;
    private ListView lvContacts;

    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    // init DBHelper và ContactDAO
    private DBHelper dbHelper;
    private DAO<Contact> contactDAO;


    private void initUI(){
        edtName = findViewById(R.id.edt_main_name);
        edtPhone = findViewById(R.id.edt_main_phone);
        lvContacts = findViewById(R.id.lv_main_contacts);
        btnInsert = findViewById(R.id.btn_main_insert);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        dbHelper = new DBHelper(this);
        contactDAO = new ContactDAO(dbHelper);

        // Listview tuỳ biến -> phải tạo Adapter
        contactList  = contactDAO.all();

        if(contactList.size() == 0) Toast.makeText(MainActivity.this, "Hãy nhập dữ liệu", Toast.LENGTH_SHORT).show();

        contactAdapter = new ContactAdapter(this, contactList);
        lvContacts.setAdapter(contactAdapter);

        // Sự kiện click vào btn insert
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                if(name.isEmpty()){
                    edtName.setError("Hãy nhập tên!");
                    return;
                }
                String phone = edtPhone.getText().toString();
                if(phone.isEmpty()){
                    edtPhone.setError("Hãy nhập sdt");
                    return;
                }

                Contact item = new Contact(name, phone);
                // Lưu vào Sql -> trả về id
                long id = contactDAO.create(item);
                if(id == -1){
                    Toast.makeText(MainActivity.this, "Thêm thất bại, trùng số đt", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Cập nhật id cho item
                item.setId(id);
                // thêm vào list
                contactList.add(item);
                // báo cho adapter biết dữ liệu đã thay đổi
                contactAdapter.notifyDataSetChanged();

            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, contactList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // Nhấn giữ để xoá: xoá trong sqlite (thành công) -> xoá trong list contact (RAM) -> update lại listviee
        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact item = contactList.get(position);
                int rs = contactDAO.delete(item.getId());
                if(rs == 0) {
                    Toast.makeText(MainActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
                else{
                    contactList.remove(position);
                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}