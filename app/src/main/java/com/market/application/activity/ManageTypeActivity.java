package com.market.application.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.market.application.R;
import com.market.application.SharePreferenceHelper;
import com.market.application.adapter.TypeAdapter;
import com.market.application.database.DatabaseManagement;
import com.market.application.database.SqliteHelper;
import com.market.application.javaclass.Product;
import com.market.application.javaclass.Type;

import java.util.ArrayList;

public class ManageTypeActivity extends BaseActivity {
    GridView typeview;
    Context context = this;
    TypeAdapter typeAdapter;
    ArrayList<Type> typeArrayList;
    LayoutInflater inflater;
    EditText edtNameType;
    Button btnAddType;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        typeview = (GridView) findViewById(R.id.listtype);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabadd);


        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.empty_type, null);
        btnAddType = (Button) view.findViewById(R.id.btnaddtype);
        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addType(view);

            }
        });


        ViewGroup viewGroup = (ViewGroup) typeview.getParent();
        viewGroup.addView(view);
        typeview.setEmptyView(view);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addType(view);
            }
        });


        typeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editType(typeArrayList.get(i).getName(), typeArrayList.get(i).getId(),i);
            }
        });


        typeview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(getResources().getString(R.string.wantdeltype));
                alert.setCancelable(false);
                alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseManagement db = new DatabaseManagement(context);
                        String sql = "select * from " + SqliteHelper.PRODUCT_TB
                                + " where " + SqliteHelper.ID_TYPE + "=" + typeArrayList.get(position).getId()
                                + " and " + SqliteHelper.ID_DISABLE + "=1";
                        ArrayList<Product> products = db.selectProductType(sql, null);


                        if (products.size() > 0) {
                            Toast.makeText(context, getResources().getString(R.string.failedeltype), Toast.LENGTH_SHORT).show();

                        } else {

                            int value = db.disableType(typeArrayList.get(position).getId());
                            db.closeDatabase();
                            if (value == 1) {
                                typeArrayList.remove(position);
                                typeAdapter.notifyDataSetChanged();

                                if (typeArrayList.size() == 0) {
                                    if (fabAdd.getVisibility() == View.VISIBLE) {
                                        fabAdd.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    if (fabAdd.getVisibility() == View.INVISIBLE) {
                                        fabAdd.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Toast.makeText(context, getResources().getString(R.string.failedeltype), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.create().dismiss();
                    }
                });

                alert.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        DatabaseManagement db = new DatabaseManagement(context);
        String sql = "select * from " + SqliteHelper.TYPE_TB + " where " + SqliteHelper.ID_DISABLE + "=?";
        typeArrayList = db.selectType(sql, new String[]{String.valueOf(1)});

        if (typeArrayList.size() == 0) {
            if (fabAdd.getVisibility() == View.VISIBLE) {
                fabAdd.setVisibility(View.INVISIBLE);
            }
        } else {
            if (fabAdd.getVisibility() == View.INVISIBLE) {
                fabAdd.setVisibility(View.VISIBLE);
            }
        }

        typeAdapter = new TypeAdapter(context, typeArrayList);
        typeview.setAdapter(typeAdapter);
    }


    private void addType(final View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View viewdialog = getLayoutInflater().inflate(R.layout.dialog_addtype, null);
        edtNameType = (EditText) viewdialog.findViewById(R.id.edtnametype);
        alert.setCancelable(false);
        alert.setView(viewdialog);
        alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nametype = edtNameType.getText().toString().trim();
                if (!nametype.equals("")) {
                    DatabaseManagement db = new DatabaseManagement(context);
                    String sql = "select * from " + SqliteHelper.TYPE_TB
                            + " where " + SqliteHelper.TYPE_NAME + "='" + nametype + "' and " + SqliteHelper.ID_DISABLE + "=1";
                    ArrayList<Type> types = db.selectType(sql, null);
                    db.closeDatabase();

                    if (types.size() == 0) {
                        Type type = new Type();
                        type.setName(nametype);

                        long id = db.insertType(type);
                        db.closeDatabase();

                        if (id != -1) {
                            SharePreferenceHelper sp = new SharePreferenceHelper(context);
                            sp.putTypeSharePreference(true);
                            type.setId((int) id);

                            typeArrayList.add(type);
                            typeAdapter.notifyDataSetChanged();

                            if (view.getId() == R.id.btnaddtype) {
                                Intent intent = new Intent(context, AddProductActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.failedtype), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.repeattype), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.create().dismiss();
            }
        });

        alert.create().show();
    }


    private void editType(String oldname, final int idtype, final int position) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View viewdialog = getLayoutInflater().inflate(R.layout.dialog_addtype, null);

        edtNameType = (EditText) viewdialog.findViewById(R.id.edtnametype);
        edtNameType.setText(oldname);

        alert.setCancelable(false);
        alert.setView(viewdialog);
        alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nametype = edtNameType.getText().toString();

                if (!nametype.equals("")) {
                    DatabaseManagement db = new DatabaseManagement(context);
                    String sql = "select * from " + SqliteHelper.TYPE_TB
                            + " where " + SqliteHelper.TYPE_NAME + "='" + nametype + "' and " + SqliteHelper.ID_DISABLE + "=1";
                    ArrayList<Type> types = db.selectType(sql, null);
                    db.closeDatabase();

                    if (types.size() == 0) {
                        Type type = new Type();
                        type.setId(idtype);
                        type.setName(nametype);

                        int value = db.updateType(type);
                        db.closeDatabase();

                        if (value != 0) {
                            Toast.makeText(context, getResources().getString(R.string.finish), Toast.LENGTH_SHORT).show();
                            typeArrayList.set(position,type);
                            typeAdapter.notifyDataSetChanged();
                            alert.create().dismiss();

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.noteditor), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.repeattype), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.fillinform), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.create().dismiss();
            }
        });

        alert.create().show();
    }

}
