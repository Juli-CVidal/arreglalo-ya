package com.egg.sp.services;

import com.egg.sp.entities.Message;
import com.egg.sp.exceptions.ServicesException;
import com.egg.sp.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    //======== CREATE ========

    @Transactional
    public void create(Message message){
        messageRepository.save(message);
    }

    //======== READ ========

    @Transactional(readOnly = true)
    public List<Message> getAll(){
        return messageRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Message findById(@NotNull Integer id) throws ServicesException {
        return getFromOptional(messageRepository.findById(id));
    }

    private Message getFromOptional(Optional<Message> messageOpt) throws ServicesException{
        if (messageOpt.isEmpty()){
            throw new ServicesException("No se ha encontrado el mensaje");
        }
        return messageOpt.get();
    }


    //======== UPDATE ========

    @Transactional
    public void update(Message message){
        create(message);
    }


    //======== DELETE ========
    //soft delete
    public void delete(@NotNull Integer id) throws ServicesException{
        Message message = findById(id);
        message.setDeleted(true);
        messageRepository.save(message);
    }
}
