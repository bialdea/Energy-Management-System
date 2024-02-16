package ro.tuc.ds2020.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.IDeviceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    IDeviceRepository IDeviceRepository;

    public List<Device> getAll(){
        return IDeviceRepository.findAll();
    }
    public List<Device> getByPersonId(Integer personId) {
        return IDeviceRepository.findByPersonId(personId);
    }

    public Device saveDevice(Device device){
        return IDeviceRepository.save(device);
    }

    public Device editDevice(Integer id, Device updatedDevice) throws NotFoundException {
        Optional<Device> DeviceOptional = IDeviceRepository.findById(id);

        if (DeviceOptional.isPresent()) {
            Device existingDevice = DeviceOptional.get();
            existingDevice.setDescription(updatedDevice.getDescription());
            existingDevice.setAddress(updatedDevice.getAddress());
            existingDevice.setMaxconsumption(updatedDevice.getMaxconsumption());

            return IDeviceRepository.save(existingDevice);
        } else {
            throw new NotFoundException("Dispozitivul nu a fost găsit.");
        }
    }

    public Device updateDevice(Integer id, Device updatedDevice) throws NotFoundException {
        Optional<Device> DeviceOptional = IDeviceRepository.findById(id);

        if (DeviceOptional.isPresent()) {
            Device existingDevice = DeviceOptional.get();
            existingDevice.setMaxconsumption(updatedDevice.getMaxconsumption());

            return IDeviceRepository.save(existingDevice);
        } else {
            throw new NotFoundException("Dispozitivul nu a fost găsit.");
        }
    }

    public Device getById(Integer id){
        if(IDeviceRepository.findById(id).isPresent()){
            return IDeviceRepository.findById(id).get();
        }
        return null;
    }

    public boolean deleteById(Integer id) throws NotFoundException {
        Optional<Device> deviceOptional = IDeviceRepository.findById(id);

        if (deviceOptional.isPresent()) {
            Device deviceToDelete = deviceOptional.get();
            IDeviceRepository.delete(deviceToDelete);
            return true;
        }
        return false;
    }
    public void deleteDevicesByPersonId(Integer personId) {
        IDeviceRepository.deleteByPersonId(personId);
    }
}
