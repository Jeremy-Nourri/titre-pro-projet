export interface TagRequest {
	designation: string;
}

export interface TagResponse {
	id: number;
	designation: string;
	taskId: number;
}