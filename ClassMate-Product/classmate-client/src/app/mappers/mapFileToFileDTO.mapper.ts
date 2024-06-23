import { FileDTO } from "../services/dto/attachment/file-dto.interface";

export function mapFileToFIleDTO(file: File){
  let fileDTO: FileDTO = {
    id: 0,
    originalFilename: file.name,
    contentType: file.type,
    size: file.size
  }
  return fileDTO;
}
