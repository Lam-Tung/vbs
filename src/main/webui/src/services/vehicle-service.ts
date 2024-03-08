export class VehicleService {
    private readonly baseUrl: string = "/api/vehicle";

    //region GETTER
    async getVehicles(pageNumber: number = 0): Promise<VehicleDTO[]> {
        const url: string = `${this.baseUrl}/page/${pageNumber}`;
        const response: Response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Failed to fetch vehicles: ${response.statusText}`);
        }

        return await response.json();
    }

    async getVehicle(id: number): Promise<VehicleDTO> {
        const url: string = `${this.baseUrl}/id/${id}`;
        const response: Response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Failed to fetch vehicle: ${response.statusText}`);
        }

        return await response.json();
    }

    //endregion
}