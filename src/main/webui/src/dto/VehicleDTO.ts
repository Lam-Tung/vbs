class VehicleDTO {
    id: number;
    licensePlate: string;
    name: string;
    manufacturer: string;
    model: string;

    constructor(vehicleDTO: Partial<VehicleDTO>) {
        Object.assign(this, vehicleDTO);
    }
}