package com.todosalau.reto2chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.todosalau.reto2chat.ChatActivity;
import com.todosalau.reto2chat.R;
import com.todosalau.reto2chat.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends ArrayAdapter<MessageModel> {
    private Context mContext;
    private List<MessageModel> mMessages;

    // Constructor de la clase MessageAdapter
    public MessageAdapter(Context context, List<MessageModel> messages) {
        super(context, 0, messages);
        mContext = context;
        mMessages = messages;
    }

    // Método llamado por ListView para obtener la vista de cada elemento en la lista
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            // Si no hay una vista reutilizable, inflarla desde el layout XML
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        }

        // Obtener el mensaje actual en la posición especificada
        MessageModel currentMessage = mMessages.get(position);

        //Linear layout para diferenciar mis chats
        LinearLayout lytMsgCard =  listItem.findViewById(R.id.lytMsgCard);

        // Obtener referencias a los TextView en el layout del mensaje
        TextView senderNameTextView = listItem.findViewById(R.id.senderNameTextView);
        TextView messageTextView = listItem.findViewById(R.id.messageTextView);
        TextView timestampTextView = listItem.findViewById(R.id.timestampTextView);
        if(currentMessage.getSenderEmail().equalsIgnoreCase(((ChatActivity) mContext).getCurrenUserMail())){
            senderNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            timestampTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

        // Establecer los valores de los TextView con los datos del mensaje actual
        senderNameTextView.setText(currentMessage.getSenderName());
        messageTextView.setText(currentMessage.getMessageText());

        // Formatear y establecer el timestamp en el TextView correspondiente
        String formattedTimestamp = formatTimestamp(currentMessage.getTimestamp());
        timestampTextView.setText(formattedTimestamp);

        // Devolver la vista completa para mostrarla en ListView
        return listItem;
    }

    // Método para formatear el timestamp según un formato específico
    private String formatTimestamp(Date timestamp) {
        // Utilizar SimpleDateFormat para formatear el timestamp a una cadena legible
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(timestamp);
    }
}
