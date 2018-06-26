package com.example.android.justjava;



import android.support.v7.app.AppCompatActivity;

import com.sun.jndi.toolkit.url.Uri;

import java.awt.Button;

import javax.swing.text.View;


/**
 * This app displays an order form to order coffee.
 */

public class MainActivity extends AppCompatActivity {

    //widgets
    TextView quantityTextView, orderSummaryTextView;
    EditText nameEditText;
    Button submitOrderButton, incrementButton, decrementButton;
    CheckBox whippedCreamCheckBox, chocolateCheckBox;
    //vars
    int quantity = 0; //inicio para evitar nullpointerexception

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init widgets
        quantityTextView = findViewById(R.id.quantity_text_view);
        orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        nameEditText = findViewById(R.id.name_edit_text);
        submitOrderButton = findViewById(R.id.submitOrderButton);
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);
        whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        chocolateCheckBox = findViewById(R.id.chocolate_checkbox);

        /**
         * This method is called when the order button is clicked.
         */
        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get chackbox status
                boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
                boolean hasChocolate = chocolateCheckBox.isChecked();
                //calculate price
                int price = calculatePrice(hasWhippedCream, hasChocolate);
                //get name
                String name = nameEditText.getText().toString();
                //get order message
                String orderMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);
                //display order summary
                displayOrderSummary(orderMessage);

                //componer email con la orden (sin el email address)
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
//                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject) + name);
                intent.putExtra(Intent.EXTRA_TEXT, orderMessage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                //CTRL+ALT+M CREAR METODO a partir de codigo
            }
        });

        //increment and decrement number of coffees needed
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });

    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {

        int basePrice = 5;

        //No se pone else if pq sino estaría evaluando UNO DE LOS DOS. En este caso los evalua aparte y permite
        //sumar en hasChocolate el basePrice que viene en la primera evaluacion! (hasWhippedCream)
        //por ej. 1ra evliacion: basePrice=6, 2da evluacion: basePrice=6+2 !
        if(hasWhippedCream){
            basePrice = basePrice + 1;
        }
        if(hasChocolate) {
            basePrice = basePrice + 2;
        }

        return quantity*basePrice;
    }

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String message = "Name: " + name;
        message += "\n" + getString(R.string.add_cream) + hasWhippedCream;
        message += "\n" + getString(R.string.add_chocolate) + hasChocolate;
        message += "\n" + getString(R.string.quantity_order) + quantity;
        message += "\n" + getString(R.string.total_order) + price;
        message += "\n" + getString(R.string.thanks);
        return message;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        quantityTextView.setText("" + number);
    }

    private void displayOrderSummary(String message){
        orderSummaryTextView.setText(message);
    }

    private void increment(){
        if(quantity>100){
            Toast.makeText(this, "Can't order more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    private void decrement(){
        if(quantity<1){
            Toast.makeText(this, "Can't order less than 1 coffee.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }


}

// TODO. Se puede auto importar packages habilitando la opcion desde Settings, buscar "auto import" habilitar "Add unambigous import for the files"